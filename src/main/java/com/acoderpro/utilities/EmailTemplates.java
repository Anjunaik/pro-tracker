package com.acoderpro.utilities;

import org.springframework.web.util.HtmlUtils;

import com.acoderpro.dto.OtpMailRequest;

public class EmailTemplates {

	public static String otpTemplate(String otp) {
		return """
				<!DOCTYPE html>
				<html>
				<head>
				  <meta charset="UTF-8">
				  <title>ProTrack – Password Reset</title>
				</head>
				<body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;">

				  <table width="100%%" cellpadding="0" cellspacing="0" style="padding:20px;">
				    <tr>
				      <td align="center">

				        <!-- Email Container -->
				        <table width="100%%" cellpadding="0" cellspacing="0"
				               style="max-width:520px; background:#ffffff; border-radius:10px;
				                      box-shadow:0 4px 12px rgba(0,0,0,0.08); padding:28px;">

				          <!-- Logo -->
				          <tr>
				            <td align="center" style="padding-bottom:20px;">
				              <img src="https://your-domain.com/assets/protrack-logo.png"
				                   alt="ProTrack"
				                   style="width:120px;" />
				            </td>
				          </tr>

				          <!-- Title -->
				          <tr>
				            <td align="center">
				              <h2 style="margin:0; color:#1f2933;">Password Reset Request</h2>
				            </td>
				          </tr>

				          <!-- Content -->
				          <tr>
				            <td style="padding-top:20px; color:#374151; font-size:14px; line-height:1.6;">
				              <p>Hello,</p>

				              <p>
				                We received a request to reset your <b>ProTrack</b> account password.
				                Use the OTP below to continue:
				              </p>

				              <!-- OTP Box -->
				              <div style="text-align:center; margin:28px 0;">
				                <span style="display:inline-block; padding:14px 24px;
				                             font-size:28px; letter-spacing:6px; font-weight:bold;
				                             color:#1a73e8; background:#f1f5ff;
				                             border-radius:8px;">
				                  """ + otp + """
				                </span>
				              </div>

				              <p>
				                This OTP is valid for <b>5 minutes</b>.
				              </p>

				              <p style="color:#6b7280; font-size:13px;">
				                For your security, do not share this OTP with anyone.
				              </p>

				              <p style="margin-top:24px;">
				                If you did not request this reset, you can safely ignore this email.
				              </p>
				            </td>
				          </tr>

				          <!-- Footer -->
				          <tr>
				            <td style="padding-top:28px; border-top:1px solid #e5e7eb;
				                       text-align:center; color:#9ca3af; font-size:12px;">
				              <p style="margin:4px 0;">© 2025 ProTrack</p>
				              <p style="margin:4px 0;">Secure Project Tracking Platform</p>
				            </td>
				          </tr>

				        </table>
				        <!-- End Container -->

				      </td>
				    </tr>
				  </table>

				</body>
				</html>
				""";
	}

	public static String passwordUpdateTemplate(String name) {
		return """
				<!DOCTYPE html>
				<html>
				<head>
				  <meta charset="UTF-8">
				  <title>ProTrack – Password Updated</title>
				</head>
				<body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;">

				  <table width="100%%" cellpadding="0" cellspacing="0" style="padding:20px;">
				    <tr>
				      <td align="center">

				        <table width="100%%" cellpadding="0" cellspacing="0"
				               style="max-width:520px; background:#ffffff; border-radius:10px;
				                      box-shadow:0 4px 12px rgba(0,0,0,0.08); padding:28px;">

				          <tr>
				            <td align="center" style="padding-bottom:20px;">
				              <img src="https://your-domain.com/assets/protrack-logo.png"
				                   alt="ProTrack"
				                   style="width:120px;" />
				            </td>
				          </tr>

				          <tr>
				            <td align="center">
				              <h2 style="margin:0; color:#1f2933;">Password Successfully Updated</h2>
				            </td>
				          </tr>

				          <tr>
				            <td style="padding-top:20px; color:#374151; font-size:14px; line-height:1.6;">
				              <p>Hi %s,</p>

				              <p>
				                Your <b>ProTrack</b> account password has been successfully updated.
				              </p>

				              <p style="margin-top:12px;">
				                If you did not perform this action, please <b>contact our support team immediately</b>
				                to secure your account.
				              </p>

				              <p style="margin-top:24px; color:#6b7280; font-size:13px;">
				                For your security, never share your password with anyone.
				              </p>
				            </td>
				          </tr>

				          <tr>
				            <td style="padding-top:28px; border-top:1px solid #e5e7eb;
				                       text-align:center; color:#9ca3af; font-size:12px;">
				              <p style="margin:4px 0;">© 2025 ProTrack</p>
				              <p style="margin:4px 0;">Secure Project Tracking Platform</p>
				            </td>
				          </tr>

				        </table>

				      </td>
				    </tr>
				  </table>

				</body>
				</html>
				""".formatted(name);
	}

	public static String accountInactiveTemplate(String name) {
	    return """
	        <!DOCTYPE html>
	        <html lang="en">
	        <head>
	            <meta charset="UTF-8">
	            <meta name="viewport" content="width=device-width, initial-scale=1.0">
	            <title>Account Inactive</title>
	        </head>

	        <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;">

	        <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
	            <tr>
	                <td align="center" style="padding:20px;">

	                    <table width="600" cellpadding="0" cellspacing="0" role="presentation"
	                           style="background-color:#ffffff; border-radius:6px; overflow:hidden;">

	                        <!-- Header -->
	                        <tr>
	                            <td style="background-color:#1f2937; padding:18px 24px;">
	                                <h2 style="margin:0; color:#ffffff; font-size:20px;">
	                                    Account Status Notification
	                                </h2>
	                            </td>
	                        </tr>

	                        <!-- Content -->
	                        <tr>
	                            <td style="padding:24px; color:#333333; font-size:14px; line-height:1.6;">

	                                <p style="margin-top:0;">
	                                    Hello <strong>%s</strong>,
	                                </p>

	                                <p>
	                                    We noticed a recent attempt to reset the password for your account.
	                                </p>

	                                <p>
	                                    Currently, your account is <strong>inactive</strong>, so we are unable
	                                    to process the password reset request.
	                                </p>

	                                <p>
	                                    Please contact our support team to regain access to your account.
	                                </p>

	                                <p style="margin-top:16px;">
	                                    📧 <strong>Support Email:</strong>
	                                    <a href="mailto:support@yourcompany.com">
	                                        support@acoder.com
	                                    </a>
	                                </p>

	                            </td>
	                        </tr>

	                        <!-- Footer -->
	                        <tr>
	                            <td style="background-color:#f4f6f8; padding:16px;
	                                       text-align:center; font-size:12px; color:#666666;">
	                                <p style="margin:0;">
	                                    If you did not attempt to reset your password, please ignore this email.
	                                </p>
	                                <p style="margin:8px 0 0;">
	                                    © 2026 Your Company. All rights reserved.
	                                </p>
	                            </td>
	                        </tr>

	                    </table>

	                </td>
	            </tr>
	        </table>

	        </body>
	        </html>
	        """.formatted(HtmlUtils.htmlEscape(name));
	}
	
	public static String registrationSuccessTemplate(String name) {
	    return """
	        <!DOCTYPE html>
	        <html lang="en">
	        <head>
	            <meta charset="UTF-8">
	            <meta name="viewport" content="width=device-width, initial-scale=1.0">
	            <title>Registration Successful</title>
	        </head>

	        <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;">

	        <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
	            <tr>
	                <td align="center" style="padding:20px;">

	                    <table width="600" cellpadding="0" cellspacing="0" role="presentation"
	                           style="background-color:#ffffff; border-radius:6px; overflow:hidden;
	                                  box-shadow:0 4px 12px rgba(0,0,0,0.08);">

	                        <!-- Header -->
	                        <tr>
	                            <td style="background-color:#2563eb; padding:18px 24px;">
	                                <h2 style="margin:0; color:#ffffff; font-size:20px;">
	                                    Welcome to ProTrack 🎉
	                                </h2>
	                            </td>
	                        </tr>

	                        <!-- Content -->
	                        <tr>
	                            <td style="padding:24px; color:#333333; font-size:14px; line-height:1.6;">

	                                <p style="margin-top:0;">
	                                    Hello <strong>%s</strong>,
	                                </p>

	                                <p>
	                                    Your registration with <strong>ProTrack</strong> was successful!
	                                </p>

	                                <p>
	                                    You can now log in and start managing your projects securely
	                                    and efficiently using our platform.
	                                </p>

	                                <p style="margin-top:16px;">
	                                    🔐 <strong>Security Tip:</strong>  
	                                    Keep your login credentials safe and never share your password with anyone.
	                                </p>

	                                <p style="margin-top:20px;">
	                                    If you have any questions or need assistance, feel free to reach out to our
	                                    support team.
	                                </p>

	                                <p style="margin-top:16px;">
	                                    📧 <strong>Support Email:</strong>
	                                    <a href="mailto:support@acoder.com">support@acoder.com</a>
	                                </p>

	                            </td>
	                        </tr>

	                        <!-- Footer -->
	                        <tr>
	                            <td style="background-color:#f4f6f8; padding:16px;
	                                       text-align:center; font-size:12px; color:#666666;">
	                                <p style="margin:0;">
	                                    Thank you for choosing ProTrack.
	                                </p>
	                                <p style="margin:8px 0 0;">
	                                    © 2026 ProTrack. All rights reserved.
	                                </p>
	                            </td>
	                        </tr>

	                    </table>

	                </td>
	            </tr>
	        </table>

	        </body>
	        </html>
	        """.formatted(HtmlUtils.htmlEscape(name));
	}

	
	public static OtpMailRequest createMailContent(String subject, String toMail, String htmlTemplate) {

		OtpMailRequest request = new OtpMailRequest();
		request.setTo(toMail);
		request.setSubject(subject);
		request.setBody(htmlTemplate);
		return request;
	}


	}

