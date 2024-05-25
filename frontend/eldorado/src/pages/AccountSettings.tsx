import {SetStateAction, useEffect, useState} from 'react';
import SiteHeader from "@/components/SiteHeader.tsx";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Controller, useForm} from "react-hook-form";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import handleApiError from "@/components/HandleApiError.ts";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Loader2, Slash} from "lucide-react";


function AccountSettings() {
    const [activeForm, setActiveForm] = useState('E-Mail');
    const {account} = useAccountState();
    const {t} = useTranslation();
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [formValues, setFormValues] = useState(null);
    const [formType, setFormType] = useState(null);
    const {getCurrentAccount} = useAccount();
    const [isLoading, setIsLoading] = useState(false);

    const emailSchema = z.object({
        email: z.string().email({message: t("accountSettings.wrongEmail")}),
    });

    const userDataSchema = z.object({
        name: z.string().min(2, {message: t("accountSettings.firstNameTooShort")})
            .max(50, {message: t("accountSettings.firstNameTooLong")}),
        lastName: z.string().min(2, {message: t("accountSettings.lastNameTooShort")})
            .max(50, {message: t("accountSettings.lastNameTooLong")}),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("accountSettings.phoneNumberInvalid")}),
    });

    const passwordSchema = z.object({
        oldPassword: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(50, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        newPassword: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(50, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        newPasswordRepeat: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(50, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
    }).refine(values => values.newPassword === values.newPasswordRepeat, {
        message: t("accountSettings.passwordsMustMatch"),
        path: ["newPasswordRepeat"],
    }).refine(values => values.oldPassword !== values.newPassword, {
        message: t("accountSettings.newPasswordCanNotBeTheSame"),
        path: ["newPassword"],
    });

    useEffect(() => {
        getCurrentAccount();
    }, []);

    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
    });

    const formPassword = useForm({
        resolver: zodResolver(passwordSchema),
    });

    const onSubmitPassword = (values: z.infer<typeof passwordSchema>) => {
        // @ts-expect-error - fix this
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('password');
        setAlertDialogOpen(true);
    };

    const onSubmitEmail = (values: SetStateAction<null>) => {
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('email');
        setAlertDialogOpen(true);
    };

    const onSubmitUserData = (values: SetStateAction<null>) => {
        setFormValues(values);
        //@ts-expect-error - fix this
        setFormType('userData');
        setAlertDialogOpen(true);
    };

    const handleDialogAction = () => {
        if (formType === 'email') {
            // @ts-expect-error - fix this
            SubmitEmail(formValues);
        } else if (formType === 'userData') {
            // @ts-expect-error - fix this
            SubmitUserData(formValues);
        } else if (formType === 'password') {
            // @ts-expect-error - fix this
            SubmitPassword(formValues);
        }
        setAlertDialogOpen(false);
    };

    const SubmitEmail = (values: z.infer<typeof emailSchema>) => {
        setIsLoading(true)
        api.changeEmailSelf(values.email).then(() => {
            getCurrentAccount();

            toast({
                title: t("accountSettings.popUp.changeEmailOK.title"),
                description: t("accountSettings.popUp.changeEmailOK.text"),
            });
            setFormType(null);
            setFormValues(null);

        }).catch((error) => {
            handleApiError(error);
        }).finally(() => {
            setIsLoading(false)
        });
    };

    const SubmitPassword = (values: z.infer<typeof passwordSchema>) => {
        setIsLoading(true)
        api.changePasswordSelf(values.oldPassword, values.newPassword).then(() => {
            getCurrentAccount();
            toast({
                title: t("accountSettings.popUp.changePasswordOK.title"),
                description: t("accountSettings.popUp.changePasswordOK.text"),
            });
            setFormType(null);
            setFormValues(null);

        }).catch((error) => {
            handleApiError(error);
        }).finally(() => {
            setIsLoading(false)
        });
    };

    const SubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        setIsLoading(true)
        const etag = window.localStorage.getItem('etag');
        if (account && account.accountLanguage && etag !== null) {
            api.modifyAccountSelf(account.login, account.version, account.userLevelsDto,
                values.name, values.lastName, values.phoneNumber, false, etag)
                .then(() => {
                    getCurrentAccount();
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeUserDataOK.text"),
                    });
                    setFormType(null);
                    setFormValues(null);

                }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false)
            });
        } else {
            console.log('Account or account language is not defined');
        }
    };

    return (
        <div>
            <SiteHeader/>
            <Breadcrumb className={"p-5"}>
                <BreadcrumbList>
                    <BreadcrumbItem>
                        <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                    </BreadcrumbItem>
                    <BreadcrumbSeparator>
                        <Slash/>
                    </BreadcrumbSeparator>
                    <BreadcrumbItem>
                        <BreadcrumbLink>{t("breadcrumb.myAccount")}</BreadcrumbLink>
                    </BreadcrumbItem>
                </BreadcrumbList>
            </Breadcrumb>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("accountSettings.title")}</h1>
                </div>
                <div
                    className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                    <nav
                        className="grid gap-4 text-sm text-muted-foreground"
                    >
                        <Button variant="link" onClick={() => setActiveForm('E-Mail')}
                                className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.email")}
                        </Button>
                        <Button variant="link" onClick={() => setActiveForm('Password')}
                                className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.password")}
                        </Button>
                        <Button variant="link" onClick={() => setActiveForm('Personal Info')}
                                className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.personalInfo")}
                        </Button>

                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'E-Mail' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent>
                                        <Form {...formEmail}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formEmail.handleSubmit(onSubmitEmail)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-5">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formEmail.control}
                                                                name="email"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.authentication.email")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.email} {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}/>
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2"
                                                                disabled={isLoading}>
                                                            {isLoading ? (
                                                                <>
                                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                                </>
                                                            ) : (
                                                                t("accountSettings.authentication.email.change")
                                                            )}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmEmailChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                        {activeForm === 'Password' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent>
                                        <Form {...formPassword}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formPassword.handleSubmit(onSubmitPassword)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-5">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formPassword.control}
                                                                name="oldPassword"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.authentication.oldPassword")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input type="password" {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                            <FormField
                                                                control={formPassword.control}
                                                                name="newPassword"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.authentication.newPassword")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input type="password" {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                            <FormField
                                                                control={formPassword.control}
                                                                name="newPasswordRepeat"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.authentication.newPasswordRepeat")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input type="password" {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2"
                                                                disabled={isLoading}>
                                                            {isLoading ? (
                                                                <>
                                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                                </>
                                                            ) : (
                                                                t("accountSettings.authentication.password.change")
                                                            )}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmPasswordChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                        {activeForm === 'Personal Info' && (
                            <div>
                                <Card className="mx-auto">
                                    <CardContent>
                                        <Form {...formUserData}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formUserData.handleSubmit(onSubmitUserData)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-10">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="name"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.personalInfo.firstName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.name} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="lastName"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.personalInfo.lastName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.lastname} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="phoneNumber"
                                                                render={() => (
                                                                    <FormItem className="items-start">
                                                                        <FormLabel
                                                                            className="text-black text-center">{t("registerPage.phoneNumber")}</FormLabel>
                                                                        <FormControl className="w-full">
                                                                            <Controller
                                                                                name="phoneNumber"
                                                                                control={formUserData.control}
                                                                                render={({field}) => (
                                                                                    <PhoneInput
                                                                                        {...field}
                                                                                        value={field.value || ""}
                                                                                        placeholder={account?.phone}
                                                                                        onChange={field.onChange}
                                                                                        countries={['PL']}
                                                                                        defaultCountry="PL"
                                                                                    />
                                                                                )}
                                                                            />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2"
                                                                disabled={isLoading}>
                                                            {isLoading ? (
                                                                <>
                                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                                </>
                                                            ) : (
                                                                t("accountSettings.personalInfo.saveChanges")
                                                            )}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmUserDataChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default AccountSettings;